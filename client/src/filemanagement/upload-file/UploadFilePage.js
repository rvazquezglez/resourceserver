import React, {useState} from "react";
import {Alert, Button, Col, Container, CustomInput, Form, FormGroup, Label, Progress, Row} from 'reactstrap';
import {Metadata} from './Metadata'
import FileUploadService from "./service/FileUploadService";


export const UploadFilePage = () => {
	const [numberOfMetadataFields, setNumberOfMetadataFields] = useState(0);
	const incrementMetadata = () => setNumberOfMetadataFields(numberOfMetadataFields + 1);

	const [selectedFile, setSelectedFile] = useState(undefined);
	const [currentFile, setCurrentFile] = useState(undefined);
	const [progress, setProgress] = useState(0);
	const [message, setMessage] = useState("");

	const handleSubmit = (e) => {
		e.preventDefault();
		let formData = new FormData();
		let target = e.target;
		formData.append("file", target.customFile.files[0]);

		for (let i = 2; i < target.length - 1; i++) {
			const metaDataInput = target[i];
			formData.append(metaDataInput.name, metaDataInput.value);
		}

		setProgress(0);
		setCurrentFile(formData);

		FileUploadService.upload(formData, (event) => {
			setProgress(Math.round((100 * event.loaded) / event.total));
		})
			.then((response) => {
				setMessage(response.data.message);
			})
			.catch((e) => {
				debugger;
				setProgress(0);
				setMessage("Could not upload the file!");
				setCurrentFile(undefined);
			});

		setSelectedFile(false);
	};

	const selectFile = () => {
		setSelectedFile(true);
	};

	return (
		<Container>
			{
				message && (<Alert color="danger">
					{message}
				</Alert>)
			}
			{
				currentFile && (<Progress value={progress}/>)
			}
			<Row>
				<Col sm="12" md={{size: 6, offset: 3}}>
					<Form onSubmit={handleSubmit}>
						<FormGroup>
							<Label for="exampleCustomFileBrowser">Please select a file</Label>
							<CustomInput type="file" id="exampleCustomFileBrowser" name="customFile"
										 onChange={selectFile}/>
						</FormGroup>
						<FormGroup>
							<Button color="secondary" onClick={incrementMetadata}>
								Add metadata field
							</Button>
						</FormGroup>
						{[...Array(numberOfMetadataFields)].map((e, i) => <Metadata key={i} elementIndex={i}/>)}
						<Button disabled={!selectedFile}>Submit</Button>
					</Form>
				</Col>
			</Row>
		</Container>

	);
}
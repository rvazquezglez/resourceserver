import React, {useState} from "react";

import {Button, Col, Container, CustomInput, Form, FormGroup, Label, Row} from 'reactstrap';
import {Metadata} from './Metadata'


export const UploadFilePage = () => {
	const [numberOfMetadataFields, setNumberOfMetadataFields] = useState(0);
	const incrementMetadata = () => setNumberOfMetadataFields(numberOfMetadataFields + 1);

	return (
		<Container>
			<Row>
				<Col sm="12" md={{size: 6, offset: 3}}>
					<Form>
						<FormGroup>
							<Label for="exampleCustomFileBrowser">Please select a file</Label>
							<CustomInput type="file" id="exampleCustomFileBrowser" name="customFile"/>
						</FormGroup>
						<FormGroup>
							<Button color="secondary" onClick={incrementMetadata}>
								Add metadata field
							</Button>
						</FormGroup>
						{[...Array(numberOfMetadataFields)].map((e, i) => <Metadata elementIndex={i}/>)}
						<Button>Submit</Button>
					</Form>
				</Col>
			</Row>
		</Container>

	);
}
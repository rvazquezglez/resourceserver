import http from "../../../http-common";

const upload = (formData, onUploadProgress) => {
	debugger;
	return http.post("/files", formData, {
		headers:{
			"Content-Type": "multipart/form-data",
			"Access-Control-Allow-Origin": "*"
		},
		onUploadProgress
	});


};

export default {upload};
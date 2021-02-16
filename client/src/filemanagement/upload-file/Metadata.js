import React from "react";

import {Col, FormGroup, Input, Row} from 'reactstrap';

export const Metadata = ({elementIndex}) => (
	<Row form>
		<Col md={6}>
			<FormGroup>
				<Input type="text" name={ `metadata[${elementIndex}].key`} placeholder="Add metadata key"/>
			</FormGroup>
		</Col>
		<Col md={6}>
			<FormGroup>
				<Input type="text" name={`metadata[${elementIndex}].value`} placeholder="Add metadata value"/>
			</FormGroup>
		</Col>
	</Row>
);
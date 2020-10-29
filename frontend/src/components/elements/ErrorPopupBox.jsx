import React from 'react';
import { Button, Modal } from 'react-bootstrap';

const ErrorPopupBox = ({ show, title = 'Error', message, handleClose }) => {
    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{message}</Modal.Body>
            <Modal.Footer>
                <Button variant='danger' onClick={handleClose}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ErrorPopupBox;

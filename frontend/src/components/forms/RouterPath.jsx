import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

import ApplicationFormHandler from './ApplicationFormHandler';
import SubmissionSuccess from './success/SubmissionSuccess';

const RouterPath = ({ match: { path }, location: { state } }) => {
    const { organisation, referenceNo } = state || {};

    return (
        <Switch>
            {organisation ? (
                <Route path={`${path}`} component={ApplicationFormHandler} />
            ) : null}

            {referenceNo ? (
                <Route
                    path={`${path}/success`}
                    exact
                    component={SubmissionSuccess}
                />
            ) : null}

            <Redirect to='/' />
        </Switch>
    );
};

export default RouterPath;

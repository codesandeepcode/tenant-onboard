import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';

import EsignForm from './UserDetails/EsignForm';
import SigningSuccess from './Succes/SigningSuccess';
import SigningFailed from './Failed/SigningFailed';

const RouterPath = ({ match: { path }, location: { state, search } }) => {
    const urlParams = new URLSearchParams(search);

    return (
        <Switch>
            <Route path={`${path}/collect`} exact>
                <EsignForm applicationNo={state ? state.applicationNo : null} />
            </Route>

            <Route path={`${path}/process-response`}>
                {urlParams.get('status') === 'success' ? (
                    <Redirect
                        to={{
                            pathname: `${path}/status/success`,
                            state: {
                                applicationNo: urlParams.get('application-no'),
                            },
                        }}
                    />
                ) : null}
                {urlParams.get('status') === 'failed' ? (
                    <Redirect
                        to={{
                            pathname: `${path}/status/failed`,
                            state: {
                                applicationNo: urlParams.get('application-no'),
                                errorMessage: urlParams.get('error-message'),
                            },
                        }}
                    />
                ) : null}
            </Route>

            <Route path={`${path}/status`} component={ProcessResponsePath} />

            <Redirect to='/' />
        </Switch>
    );
};

const ProcessResponsePath = ({
    match: { path },
    location: {
        state: { applicationNo, errorMessage },
    },
}) => (
    <Switch>
        <Route path={`${path}/success`} exact>
            <SigningSuccess applicationNo={applicationNo} />
        </Route>

        <Route path={`${path}/failed`} exact>
            <SigningFailed
                applicationNo={applicationNo}
                errorMessage={errorMessage}
            />
        </Route>
    </Switch>
);

export default RouterPath;

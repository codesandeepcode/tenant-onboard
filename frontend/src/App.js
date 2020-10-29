import React from 'react';
import { BrowserRouter, Redirect, Route, Switch } from 'react-router-dom';

import './styles.css';
import ScrollToTop from './components/elements/ScrollToTop';
import LandingPage from './components/elements/LandingPage/LandingPage';
import NotFound from './components/elements/NotFound/NotFound';
import ApplicationPath from './components/forms/RouterPath';
import EsignPath from './components/elements/Esign/RouterPath';

export default function App() {
    return (
        <BrowserRouter basename={process.env.PUBLIC_URL}>
            <>
                <ScrollToTop />

                <Switch>
                    <Redirect from='/' exact to='/welcome' />
                    <Route path='/welcome' exact component={LandingPage} />
                    <Route
                        path='/application-form'
                        component={ApplicationPath}
                    />
                    <Route path='/esign' component={EsignPath} />
                    <Route component={NotFound} />
                </Switch>
            </>
        </BrowserRouter>
    );
}

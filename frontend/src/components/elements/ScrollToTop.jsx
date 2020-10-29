// This code is taken from - https://stackoverflow.com/questions/36904185/react-router-scroll-to-top-on-every-transition
// to solve the problems of page not dislaying at top during navigation

import { useEffect } from 'react';
import { withRouter } from 'react-router-dom';

const ScrollToTop = ({ history }) => {
    useEffect(() => {
        const unlisten = history.listen(() => {
            window.scrollTo(0, 0);
        });

        return () => unlisten();
    }, [history]);

    return null;
};

export default withRouter(ScrollToTop);

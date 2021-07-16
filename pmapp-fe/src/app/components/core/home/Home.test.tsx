import React from 'react';
import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { store } from '../../../redux/store';
import Home from './Home';
import { createTheme, StyledEngineProvider, ThemeProvider } from '@material-ui/core';
import { BrowserRouter } from 'react-router-dom';

test('renders welcome text', () => {
  const theme = createTheme();
  const { getByText } = render(
    <StyledEngineProvider injectFirst>
      <ThemeProvider theme={theme}>
        <Provider store={store}>
          <BrowserRouter>
            <Home />
          </BrowserRouter>
        </Provider>
      </ThemeProvider>
    </StyledEngineProvider>
  );

  expect(getByText(/Welcome to Project Management App/i)).toBeInTheDocument();
});

import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { AppThunk, RootState } from '../store';
import { LoginRequestDto } from '../../models/dtos/LoginRequestDto';
import { authService } from '../../services/authService';
import { SessionStatus } from '../../models/core/SessionStatus';
import { UserDto } from '../../models/dtos/UserDto';
import { AppNotification } from '../../models/core/AppNotification';

export interface SessionState {
  accessToken?: string | null;
  user?: UserDto | null;
  isAuthenticated: boolean;
  sessionStatus: SessionStatus;
  notification: AppNotification | null;
}

const initialState: SessionState = {
  accessToken: null,
  user: null,
  isAuthenticated: false,
  sessionStatus: SessionStatus.None,
  notification: null
};

// Async thunks
export const loginAsync = createAsyncThunk(
  'auth/login',
  async (loginReq: LoginRequestDto) => {
    const response = await authService.Login(loginReq);

    return { status: response.status, data: response.data };
  }
);

export const logoutAsync = createAsyncThunk(
  'auth/logout',
  async () => {
    const response = await authService.Logout();

    return response.status;
  }
);

// Slice
export const SessionSlice = createSlice({
  name: 'session',
  initialState,
  reducers: {
    setSessionStatus: (state, action: PayloadAction<SessionStatus>) => {
      state.sessionStatus = action.payload;
    },
    setNotification: (state, action: PayloadAction<AppNotification | null>) => {
      state.notification = action.payload;
    },
    setAccessToken: (state, action: PayloadAction<string | null>) => {
      state.accessToken = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginAsync.rejected, (state) => {
        clearSession(state);
      })
      .addCase(loginAsync.fulfilled, (state, action) => {
        const response = action.payload;
        if (response.status === 200 && response.data !== null) {
          state.accessToken = response.data.accessToken;
          state.user = response.data.user;
          state.isAuthenticated = true;
          state.sessionStatus = SessionStatus.Authenticated;
        }
      })
      .addCase(logoutAsync.fulfilled, (state, action) => {
        const responseStatus = action.payload;
        if (responseStatus === 200) {
          clearSession(state);
        }
      });
  },
});

const clearSession = (state: any, sessionStatus: SessionStatus = SessionStatus.None) => {
  state.accessToken = null;
  state.user = null;
  state.isAuthenticated = false;
  state.sessionStatus = sessionStatus;
};

// Actions
export const { setSessionStatus, setNotification, setAccessToken } = SessionSlice.actions;

// Custom Thunks
export const setStatus = (sessionStatus: SessionStatus, doLogout: boolean = false): AppThunk => (
  dispatch: any,
  getState: any
) => {
  if (doLogout) {
    dispatch(logoutAsync()).then(() => dispatch(setSessionStatus(sessionStatus)));
  } else {
    dispatch(setSessionStatus(sessionStatus));
  }
};


// Selectors
export const selectLoggedUser = (state: RootState) => state.session.user;

export const selectAccessToken = (state: RootState) => state.session.accessToken;

export const selectIsAuthenticated = (state: RootState) => state.session.isAuthenticated;

export const selectSessionStatus = (state: RootState) => state.session.sessionStatus;

export const selectNotification = (state: RootState) => state.session.notification;


export default SessionSlice.reducer;

import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import { loadState, saveState } from './persistence/localStorage';
import sessionReducer from './slices/sessionSlice';
import throttle from 'lodash.throttle';

const persistedState = loadState();

export const store = configureStore({
  reducer: {
    session: sessionReducer
  },
  preloadedState: persistedState
});

store.subscribe(throttle(() => {
  saveState(store.getState());
}, 1000));

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;

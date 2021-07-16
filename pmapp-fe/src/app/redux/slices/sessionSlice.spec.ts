import { SessionStatus } from '../../models/core/SessionStatus';
import { Role } from '../../models/dtos/Role';
import sessionReducer, { SessionState, setAccessToken, setNotification, setSessionStatus } from './sessionSlice';

describe('session reducer', () => {
    const initialState: SessionState = {
        accessToken: 'abc1234',
        user: { username: 'test', email: 'test@test', role: Role.Administrator },
        isAuthenticated: true,
        sessionStatus: SessionStatus.None,
        notification: null
    };

    it('should handle initial state', () => {
        expect(sessionReducer(undefined, { type: 'unknown' })).toEqual({
            accessToken: null,
            user: null,
            isAuthenticated: false,
            sessionStatus: SessionStatus.None,
            notification: null
        });
    });

    it('should handle setSessionStatus', () => {
        const actual = sessionReducer(initialState, setSessionStatus(SessionStatus.Authenticated));
        expect(actual.sessionStatus).toEqual(SessionStatus.Authenticated);
    });

    it('should handle setNotification', () => {
        const actual = sessionReducer(initialState, setNotification({ message: 'test notification!', type: 'success' }));
        expect(actual.notification).toEqual({ message: 'test notification!', type: 'success' });
    });

    it('should handle setAccessToken', () => {
        const actual = sessionReducer(initialState, setAccessToken('newToken'));
        expect(actual.accessToken).toEqual('newToken');
    });
});

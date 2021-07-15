import React, { useEffect, useState } from 'react';
import { Grid, Paper } from '@material-ui/core';
import { DataGrid } from '@material-ui/data-grid';
import useUsersStyles from './Users.styles';
import { userService } from '../../../services/userService';
import { UserDto } from '../../../models/dtos/UserDto';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { selectLoggedUser, setStatus } from '../../../redux/slices/sessionSlice';
import { SessionStatus } from '../../../models/core/SessionStatus';
import { Redirect } from 'react-router';
import { Role } from '../../../models/dtos/Role';
import { getUsersColumnsDefs } from './getUsersColumnsDef';

export default function Users() {
    const classes = useUsersStyles();
    const [users, setUsers] = useState<UserDto[]>([]);
    const userRole = useAppSelector(selectLoggedUser)?.role;
    const dispatch = useAppDispatch();

    useEffect(() => {
        userService.FindAll().then((response) => {
            response?.data.map(user => (user as any).id = user.username);
            setUsers(response?.data);
        });
    }, []);

    if (userRole !== Role.Administrator) {
        dispatch(setStatus(SessionStatus.Forbidden));
        return <Redirect to={{ pathname: '/' }} />;
    }

    const deleteUser = (code: string) => {

    };

    return (
        <>
            <h1>Users</h1>
            <span>Users management</span>
            <br />
            <br />

            <Grid container spacing={3}>
                {/* Chart */}
                <Grid item xs={12} md={12} lg={12}>
                    <Paper className={classes.paper}>
                        <div style={{ height: 400, width: '100%' }}>
                            <DataGrid
                                rows={users}
                                columns={getUsersColumnsDefs({ classes, deleteUser })}
                                pageSize={5}
                                checkboxSelection
                                disableSelectionOnClick
                            />
                        </div>
                    </Paper>
                </Grid>
            </Grid>
        </>
    );
}

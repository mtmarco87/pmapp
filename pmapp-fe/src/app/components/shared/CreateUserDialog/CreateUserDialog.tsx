import React, { useCallback, useEffect, useState } from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { FormControl, InputLabel, MenuItem, Select, Theme } from '@material-ui/core';
import { UserDto } from '../../../models/dtos/UserDto';
import { Role } from '../../../models/dtos/Role';
import { makeStyles } from '@material-ui/styles';
import lodash from 'lodash';

const useStyles = makeStyles((theme: Theme) => ({
  formControl: {
    margin: 0,
    minWidth: 120,
    width: '100%',
    paddingBottom: 10
  }
}));

export default function CreateUserDialog({ open, setOpen, handleCreate }:
  { open: boolean, setOpen: Function, handleCreate: any }) {
  const classes = useStyles();

  const [newUser, setNewUser] = useState<UserDto>({} as UserDto);

  useEffect(() => {
    setNewUser({} as UserDto);
  }, [open]);

  const validateAndCreate = useCallback(() => {
    handleCreate(newUser);
  }, [handleCreate, newUser]);

  const handleClose = useCallback((event: object, reason?: any) => {
    setOpen(false);
  }, [setOpen]);

  const handleChange = useCallback((event: any, field: string) => {
    let value = event.target.value === 'emptyOption' ? null : event.target.value;
    const user = lodash.cloneDeep(newUser);
    (user as any)[field] = value;
    setNewUser(user);
  }, [newUser]);

  if (newUser.role === undefined) {
    newUser.role = Role.Developer;
  }

  const createDisabled =
    !newUser.username || newUser.username.trim() === '' ||
    !newUser.password || newUser.password.trim() === '' ||
    !newUser.email || newUser.email.trim() === '' ||
    !newUser.name || newUser.name.trim() === '' ||
    !newUser.surname || newUser.surname.trim() === '' ||
    !newUser.role;

  return (
    <div>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            validateAndCreate();
          }
        }}>
        <DialogTitle id="form-dialog-title">Create new User</DialogTitle>
        <DialogContent>
          <DialogContentText className={classes.formControl}>
            To create a new user, please enter all the required fields.
          </DialogContentText>
          <TextField
            id="username"
            className={classes.formControl}
            margin="dense"
            label="Username"
            type="string"
            required={true}
            fullWidth
            onChange={(event) => handleChange(event, 'username')}
          />
          <TextField
            id="password"
            className={classes.formControl}
            margin="dense"
            label="Password"
            type="password"
            required={true}
            fullWidth
            onChange={(event) => handleChange(event, 'password')}
          />
          <TextField
            id="email"
            className={classes.formControl}
            margin="dense"
            label="Email"
            type="email"
            required={true}
            fullWidth
            onChange={(event) => handleChange(event, 'email')}
          />
          <TextField
            id="name"
            className={classes.formControl}
            margin="dense"
            label="Name"
            type="string"
            required={true}
            fullWidth
            onChange={(event) => handleChange(event, 'name')}
          />
          <TextField
            id="surname"
            className={classes.formControl}
            margin="dense"
            label="Surname"
            type="string"
            required={true}
            fullWidth
            onChange={(event) => handleChange(event, 'surname')}
          />
          <FormControl variant="outlined" className={classes.formControl} required={true}>
            <InputLabel id="role-label">Role</InputLabel>
            <Select
              id="role-select"
              className={classes.formControl}
              value={newUser.role}
              label="Role"
              labelId="role-label"
              onChange={(event) => handleChange(event, 'role')}
            >
              {[Role.Developer, Role.ProjectManager, Role.Administrator]
                .map((role: string) => (
                  <MenuItem
                    key={role}
                    value={role}>
                    {role}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={validateAndCreate} color="primary" disabled={createDisabled}>
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div >
  );
}
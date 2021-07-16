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
import { ProjectDto } from '../../../models/dtos/ProjectDto';
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

export default function CreateProjectDialog({ open, setOpen, handleCreate, loggedUser, users }:
  { open: boolean, setOpen: Function, handleCreate: any, loggedUser: UserDto | null | undefined, users: UserDto[] }) {
  const classes = useStyles();

  const [newProject, setNewProject] = useState<ProjectDto>({} as ProjectDto);

  useEffect(() => {
    setNewProject({} as ProjectDto);
  }, [open]);

  const validateAndCreate = useCallback(() => {
    handleCreate(newProject);
  }, [handleCreate, newProject]);

  const handleClose = useCallback((event: object, reason?: any) => {
    setOpen(false);
  }, [setOpen]);

  const handleChange = useCallback((event: any, field: string) => {
    const project = lodash.cloneDeep(newProject);
    (project as any)[field] = event.target.value;
    setNewProject(project);
  }, [newProject]);

  const filteredUsers = users.filter(usr => usr.role === Role.Administrator || usr.role === Role.ProjectManager);
  if (!newProject.projectManager) {
    newProject.projectManager = loggedUser?.username ?? filteredUsers[0].username ?? undefined;
  }

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
        <DialogTitle id="form-dialog-title">Create new Project</DialogTitle>
        <DialogContent>
          <DialogContentText className={classes.formControl}>
            To create a new project, please enter all the required fields.
          </DialogContentText>
          <TextField
            className={classes.formControl}
            autoFocus
            margin="dense"
            id="name"
            label="Name"
            type="string"
            fullWidth
            onChange={(event) => handleChange(event, 'name')}
          />
          <FormControl variant="outlined" className={classes.formControl} required={true}>
            <InputLabel id="pm-label">Project Manager</InputLabel>
            <Select
              id="pm-select"
              className={classes.formControl}
              value={newProject.projectManager}
              label="Project Manager"
              labelId="pm-label"
              onChange={(event) => handleChange(event, 'projectManager')}
            >
              {filteredUsers
                .map((user: UserDto) => (
                  <MenuItem
                    key={user.username ?? 'emptyOption'}
                    value={user.username ?? 'emptyOption'}>
                    {user.name} {user.surname}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={validateAndCreate} color="primary">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
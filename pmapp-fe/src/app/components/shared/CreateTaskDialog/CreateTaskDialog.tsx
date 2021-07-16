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
import { TaskDto } from '../../../models/dtos/TaskDto';
import { TaskStatus } from '../../../models/dtos/TaskStatus';
import moment from 'moment';

const useStyles = makeStyles((theme: Theme) => ({
  formControl: {
    margin: 0,
    minWidth: 120,
    width: '100%',
    paddingBottom: 10
  }
}));

export default function CreateTaskDialog({ open, setOpen, handleCreate, loggedUser, users, currentProjectCode, projects }:
  {
    open: boolean, setOpen: Function, handleCreate: any, loggedUser: UserDto | null | undefined, users: UserDto[],
    currentProjectCode?: number, projects: ProjectDto[]
  }) {
  const classes = useStyles();

  const [newTask, setNewTask] = useState<TaskDto>({} as TaskDto);

  useEffect(() => {
    setNewTask({} as TaskDto);
  }, [open]);

  const validateAndCreate = useCallback(() => {
    handleCreate(newTask);
  }, [handleCreate, newTask]);

  const handleClose = useCallback((event: object, reason?: any) => {
    setOpen(false);
  }, [setOpen]);

  const handleChange = useCallback((event: any, field: string) => {
    let value = event.target.value === 'emptyOption' ? null : event.target.value;
    if (field === 'progress') {
      if (value as number < 0) {
        value = 0;
      } else if (value as number > 100) {
        value = 100;
      }
    } else if (field === 'deadline') {
      value = moment(value).format('YYYY-MM-DDTHH:mm:ss.SSSZ').toString();
    }

    const task = lodash.cloneDeep(newTask);
    (task as any)[field] = value;
    setNewTask(task);
  }, [newTask]);

  let possibleAssignees: UserDto[] = [{ username: null, name: 'N/A', surname: '' }, ...users];
  let possibleProjects: ProjectDto[] = [{ code: null, name: 'N/A' }, ...projects];
  if (currentProjectCode) {
    possibleProjects = [projects.find(proj => proj.code === currentProjectCode) ?? {} as ProjectDto];
  } else if (loggedUser?.role === Role.ProjectManager) {
    possibleProjects = possibleProjects.filter(proj => proj.projectManager === loggedUser.username);
  }

  if (!currentProjectCode) {
    possibleAssignees = [users.find(user => user.username === loggedUser?.username) ?? {} as UserDto];
  } else if (loggedUser?.role === Role.ProjectManager) {
    possibleAssignees = users.filter(usr => usr.role !== Role.Administrator || usr.username === null);
  }

  if (newTask.assignee === undefined && possibleAssignees.length > 0) {
    newTask.assignee = possibleAssignees[0].username;
  }
  if (newTask.project === undefined && possibleProjects.length > 0) {
    newTask.project = possibleProjects[0].code;
  }
  if (newTask.status === undefined) {
    newTask.status = TaskStatus.New;
  }
  if (newTask.deadline === undefined) {
    newTask.deadline = moment().format('YYYY-MM-DDTHH:mm:ss.SSSZ').toString();
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
        <DialogTitle id="form-dialog-title">Create new Task</DialogTitle>
        <DialogContent>
          <DialogContentText className={classes.formControl}>
            To create a new task, please enter all the required fields.
          </DialogContentText>
          <FormControl variant="outlined" className={classes.formControl} required={!currentProjectCode}>
            <InputLabel id="assignee-label">Assignee</InputLabel>
            <Select
              id="assignee-select"
              className={classes.formControl}
              value={newTask.assignee ?? 'emptyOption'}
              label="Assignee"
              labelId="assignee-label"
              disabled={!currentProjectCode}
              onChange={(event) => handleChange(event, 'assignee')}
            >
              {possibleAssignees
                .map((user: UserDto) => (
                  <MenuItem
                    key={user.username ?? 'emptyOption'}
                    value={user.username ?? 'emptyOption'}>
                    {user.name} {user.surname}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>
          <FormControl variant="outlined" className={classes.formControl} required={true}>
            <InputLabel id="proj-label">Project</InputLabel>
            <Select
              id="proj-select"
              className={classes.formControl}
              value={newTask.project ?? 'emptyOption'}
              label="Project"
              labelId="proj-label"
              disabled={!!currentProjectCode}
              onChange={(event) => handleChange(event, 'project')}
            >
              {possibleProjects
                .map((proj: ProjectDto) => (
                  <MenuItem
                    key={proj.code ?? 'emptyOption'}
                    value={proj.code ?? 'emptyOption'}>
                    {proj.name}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>
          <TextField
            id="description"
            className={classes.formControl}
            margin="dense"
            label="Description"
            type="string"
            fullWidth
            onChange={(event) => handleChange(event, 'description')}
          />
          <TextField
            id="outlined-number"
            className={classes.formControl}
            margin="dense"
            label="Progress"
            type="number"
            InputLabelProps={{
              shrink: true,
            }}
            fullWidth
            onChange={(event) => handleChange(event, 'progress')}
          />
          <FormControl variant="outlined" className={classes.formControl} required={true}>
            <InputLabel id="status-label">Status</InputLabel>
            <Select
              id="status-select"
              className={classes.formControl}
              value={newTask.status}
              label="Status"
              labelId="status-label"
              onChange={(event) => handleChange(event, 'status')}
            >
              {[TaskStatus.New, TaskStatus.InProgress, TaskStatus.Completed]
                .map((status: string) => (
                  <MenuItem
                    key={status}
                    value={status}>
                    {status}
                  </MenuItem>
                ))}
            </Select>
          </FormControl>
          <TextField
            id="deadline"
            label="Deadline"
            type="date"
            defaultValue={moment().format('YYYY-MM-DD')}
            className={classes.formControl}
            InputLabelProps={{
              shrink: true,
            }}
            onChange={(event) => handleChange(event, 'deadline')}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={validateAndCreate} color="primary" disabled={!newTask.project}>
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div >
  );
}
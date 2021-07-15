import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import HomeIcon from '@material-ui/icons/Home';
import AssignmentIcon from '@material-ui/icons/Assignment';
import FormatListNumberedIcon from '@material-ui/icons/FormatListNumbered';
import PeopleIcon from '@material-ui/icons/People';
import { Link } from 'react-router-dom';
import { Typography } from '@material-ui/core';
import { UserDto } from '../../../models/dtos/UserDto';
import { useMenuItemsStyles } from './MenuItems.styles';
import { Role } from '../../../models/dtos/Role';

export const UpperMenuItems = ({ isAuthenticated, loggedUser }: { isAuthenticated: boolean, loggedUser: UserDto | null | undefined }) => {
  const classes = useMenuItemsStyles();
  const authLink = !isAuthenticated ? '/login' : '/logout';
  const authLinkText = !isAuthenticated ? 'Login' : 'Logout';

  return (
    <div>
      {isAuthenticated ?
        <>
          <Typography component="h2" variant="h6" color="primary" gutterBottom className={classes.title}>
            Hi {loggedUser?.name}
          </Typography>
          <Typography color="textSecondary" className={classes.caption}>
            ({loggedUser?.email} - {loggedUser?.role})
          </Typography>
        </>
        :
        <>
          <Typography component="h2" variant="h6" color="primary" gutterBottom className={classes.title}>
            Welcome to PmApp
          </Typography>
          <Typography color="textSecondary" className={classes.caption}>
            Hi! Please login to start using the app.
          </Typography>
        </>}
      <ListItem button component={Link} to={authLink}>
        <ListItemIcon>
          <LockOpenIcon />
        </ListItemIcon>
        <ListItemText primary={authLinkText} />
      </ListItem >
    </div>
  );
};

export const BottomMenuItems = ({ isAuthenticated, loggedUser }: { isAuthenticated: boolean, loggedUser: UserDto | null | undefined }) => {
  return (
    <div>
      <ListItem button component={Link} to="/">
        <ListItemIcon>
          <HomeIcon />
        </ListItemIcon>
        <ListItemText primary="Home" />
      </ListItem>
      {isAuthenticated &&
        <>
          {loggedUser?.role === Role.Administrator || loggedUser?.role === Role.ProjectManager ?
            <ListItem button component={Link} to="/projects">
              <ListItemIcon>
                <AssignmentIcon />
              </ListItemIcon>
              <ListItemText primary="Projects" />
            </ListItem>
            :
            <></>
          }
          <ListItem button component={Link} to="/tasks">
            <ListItemIcon>
              <FormatListNumberedIcon />
            </ListItemIcon>
            <ListItemText primary="Tasks" />
          </ListItem>
          {loggedUser?.role === Role.Administrator ?
            <ListItem button component={Link} to="/users">
              <ListItemIcon>
                <PeopleIcon />
              </ListItemIcon>
              <ListItemText primary="Users" />
            </ListItem>
            :
            <></>
          }
        </>
      }
    </div>
  );
};

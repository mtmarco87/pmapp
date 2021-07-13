import { Redirect, Route, Switch } from "react-router";
import Login from "./app/components/core/login/Login";
import Logout from "./app/components/core/logout/Logout";
import Home from "./app/components/core/home/Home";
import Projects from "./app/components/core/projects/Projects";
import Tasks from "./app/components/core/tasks/Tasks";
import Users from "./app/components/core/users/Users";
import PrivateRoute from "./app/components/hoc/private-route/PrivateRoute";

export default function Routes() {
    return (
        <Switch>
            <Route exact path="/"><Home /></Route>
            <Route path="/login"><Login /></Route>
            <Route path="/logout"><Logout /></Route>
            <PrivateRoute path="/projects" Component={Projects} />
            <PrivateRoute path="/tasks" Component={Tasks} />
            <PrivateRoute path="/users" Component={Users} />
            <Redirect to="/" />
        </Switch>
    );
};

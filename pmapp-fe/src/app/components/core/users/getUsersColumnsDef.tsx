import { IconButton } from "@material-ui/core";
import { GridColDef } from "@material-ui/data-grid";
import DeleteIcon from '@material-ui/icons/Delete';

export const getUsersColumnsDefs = ({ classes, deleteUser }: { classes: any, deleteUser: Function }) => {
    const columns: GridColDef[] = [
        {
            field: 'username',
            headerName: 'Username',
            width: 250
        },
        {
            field: 'email',
            headerName: 'Email',
            width: 250,
            editable: true,
        },
        {
            field: 'name',
            headerName: 'Name',
            width: 250,
            editable: true,
        },
        {
            field: 'surname',
            headerName: 'Surname',
            width: 250,
            editable: true,
        },
        {
            field: 'role',
            headerName: 'Role',
            type: 'string',
            width: 250,
            editable: true,
        },
        {
            field: "",
            headerName: "Actions",
            width: 150,
            renderCell: (params) => {
                return (
                    <div className={classes.actionButtons}>
                        <IconButton
                            edge="start"
                            color="inherit"
                            aria-label="delete user"
                            onClick={() => deleteUser(params.id)}>
                            <DeleteIcon />
                        </IconButton>
                    </div >
                );
            }
        }
    ];

    return columns;
}

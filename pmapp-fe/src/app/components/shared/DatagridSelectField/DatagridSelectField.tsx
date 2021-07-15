import React, { useCallback } from "react";
import { makeStyles } from "@material-ui/styles";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import { Theme } from "@material-ui/core/styles";
import { GridApi, GridCellParams, GridCellValue, GridEditCellPropsParams, GridRowId, GRID_CELL_EDIT_PROPS_CHANGE_COMMITTED } from "@material-ui/data-grid";

const useStyles = makeStyles((theme: Theme) => ({
  formControl: {
    margin: 0,
    minWidth: 120,
    width: '100%'
  },
  selectEmpty: {
    marginTop: theme.spacing(2)
  }
}));

function DatagridSelectFieldImpl<T>({ selectParams, dataGridParams }:
  { selectParams: SelectParams<T>, dataGridParams: GridCellParams }) {
  const { options, idField, labelRenderFn } = selectParams;
  const { id, value, api, field }:
    { id: GridRowId, value: GridCellValue, api: GridApi, field: string } = dataGridParams;
  const classes = useStyles();

  const handleChange = useCallback(
    (event) => {
      const editProps = {
        value: event.target.value
      };

      // Commit selected value internally in the Grid
      api.commitCellChange({ id, field, props: editProps });
      // Publish the committed event to allow further data processing / i.e. send data change to API 
      api.publishEvent(
        GRID_CELL_EDIT_PROPS_CHANGE_COMMITTED,
        { id, field, props: editProps } as GridEditCellPropsParams
      );
      // Restore view mode
      api.setCellMode(id, field, "view");
      event.stopPropagation();
    },
    [api, field, id]
  );

  return (
    <FormControl className={classes.formControl}>
      <Select
        id="pm-select"
        value={value}
        onChange={handleChange}
      >
        {options
          .map((item: any) => (
            <MenuItem
              key={!!idField ? item[idField] : item}
              value={!!idField ? item[idField] : item}>
              {!!labelRenderFn ? labelRenderFn(item) : item}</MenuItem>
          ))}
      </Select>
    </FormControl>
  );
};

interface SelectParams<T> {
  options: T[],
  idField?: string,
  labelRenderFn?: Function
}

export function DataGridSelectField<T>(selectParams: SelectParams<T>, dataGridParams: GridCellParams) {
  return <DatagridSelectFieldImpl selectParams={selectParams} dataGridParams={dataGridParams} />;
}

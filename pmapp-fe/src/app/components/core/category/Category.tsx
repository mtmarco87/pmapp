// src/Category.js

import React, { useEffect } from "react";
import { Link, Route, useParams, useRouteMatch } from "react-router-dom";
import { userService } from "../../../services/userService";

const Item = () => {
  const { name } = useParams<{ name: string }>();

  return (
    <div>
      <h3>{name}</h3>
    </div>
  );
}

export default function Category() {
  const { url, path } = useRouteMatch();

  useEffect(() => {
    userService.GetAll().then((response) => {
      console.log(response.data);
    });
  }, []);

  return (
    <div>
      <ul>
        <li>
          <Link to={`${url}/shoes`}>Shoes</Link>
        </li>
        <li>
          <Link to={`${url}/boots`}>Boots</Link>
        </li>
        <li>
          <Link to={`${url}/footwear`}>Footwear</Link>
        </li>
      </ul>
      <Route path={`${path}/:name`}>
        <Item />
      </Route>
    </div>
  );
};

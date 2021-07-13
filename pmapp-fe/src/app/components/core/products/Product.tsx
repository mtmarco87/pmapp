import React from "react";
import { useParams } from "react-router-dom";
import { ProductI } from "./ProductI";

const Product = ({ data }: { data: ProductI[] }) => {
    const { productId } = useParams<{ productId: string }>();
    const product = data.find(p => p.id === Number(productId));
    let productData;

    if (product) {
        productData = (
            <div>
                <h3> {product.name} </h3>
                <p>{product.description}</p>
                <hr />
                <h4>{product.status}</h4>
            </div>
        );
    } else {
        productData = <h2> Sorry. Product doesn't exist </h2>;
    }

    return (
        <div>
            <div>{productData}</div>
        </div>
    );
};

export default Product;
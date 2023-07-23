import { v4 as uuidv4 } from "uuid";
import * as status from "./constants/http-status.js";

export default (req, res, next) => {
    let { transactionid } = req.headers;
    console.log(req.headers)
    if (!transactionid) {
        return res.status(status.BAD_REQUEST).json({
            status: status.BAD_REQUEST,
            message: "The transactionid header is required.",
        });
    }
    console.log(uuidv4())
    req.headers.serviceid = uuidv4();
    return next();
};
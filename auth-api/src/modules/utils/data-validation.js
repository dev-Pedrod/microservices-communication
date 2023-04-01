import UserException from "../user/exception/UserException.js";
import * as httpStatus from "../../config/constants/http-status.js";
import bcrypt from "bcrypt";

export function validateAccessTokenData(email, password) {
    if (!email || !password) {
        throw new UserException(httpStatus.UNAUTHORIZED, "Email or password must be informed");
    }
}

export function validateRequestData(email) {
    if (!email) {
        throw new UserException("User email was not informed.");
    }
}

export async function validatePassword(password, hashPassword) {
    if (!(await bcrypt.compare(password, hashPassword))) {
        throw new UserException(
            httpStatus.UNAUTHORIZED,
            "Password doesn't match."
        );
    }
}

export function validateUserNotFound(user) {
    if (!user) {
        throw new UserException(httpStatus.BAD_REQUEST, "User was not found.");
    }
}
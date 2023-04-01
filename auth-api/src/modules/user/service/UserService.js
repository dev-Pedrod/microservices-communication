import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/http-status.js";
import * as secrets from "../../../config/constants/secrets.js"
import * as validation from "../../utils/data-validation.js"

import jwt from "jsonwebtoken"

class UserService {
    async findByEmail(req) {
        try {
            const { email } = req.params;
            const { authUser } = req;
            validation.validateRequestData(email);
            let user = await UserRepository.findByEmail(email);
            validation.validateUserNotFound(user);
            validation.validateAuthenticatedUser(user, authUser);
            return {
                status: httpStatus.SUCCESS,
                content: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                },
            };
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };
        }
    }

    async getAccessToken(req) {
        try {
            const { transactionid, serviceid } = req.headers;
            console.info(
                `Request to POST login with data ${JSON.stringify(
                    req.body
                )} | [transactionID: ${transactionid} | serviceID: ${serviceid}]`
            );
            const { email, password } = req.body;
            validation.validateAccessTokenData(email, password);
            let user = await UserRepository.findByEmail(email);
            validation.validateUserNotFound(user);
            await validation.validatePassword(password, user.password);
            const authUser = { id: user.id, name: user.name, email: user.email };
            const accessToken = jwt.sign({ authUser }, secrets.API_SECRET, {
                expiresIn: "1d",
            });

            let response = {
                status: httpStatus.SUCCESS,
                accessToken,
            };
            console.info(
                `Response to POST login with data ${JSON.stringify(
                    response
                )} | [transactionID: ${transactionid} | serviceID: ${serviceid}]`
            );
            return response;
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };
        }
    }
}

export default new UserService();
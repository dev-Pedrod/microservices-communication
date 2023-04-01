import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/http-status.js";
import * as secrets from "../../../config/constants/secrets.js"
import * as validation from "../../utils/data-validation.js"

import jwt from "jsonwebtoken"

class UserService {
    async findByEmail(req) {
        try {
            const {email} = req.params;
            console.log(email)
            validation.validateRequestData(email);
            let user = await UserRepository.findByEmail(email);
            validation.validateUserNotFound(user);
            return {
                id: user.id,
                name: user.name,
                email: user.email,
            };
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };
        }
    }

    async getAccessToken(req) {
        const {email, password} = req.body;
        console.log(email)
        try {
            validation.validateAccessTokenData(email, password)
            let user = await UserRepository.findByEmail(email);
            validation.validateUserNotFound(user);
            await validation.validatePassword(password, user.password)
            const authUser = {id: user.id, name: user.name, email: user.email}
            return {
                status: 200,
                token: jwt.sign({authUser}, secrets.API_SECRET, {expiresIn: "1d"})
            }
        } catch (err) {
            return {
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };
        }
    }
}

export default new UserService();
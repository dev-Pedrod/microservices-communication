import UserService from "../service/UserService.js";

class UserController {
    async getAccessToken(req, res) {
        let auth = await UserService.getAccessToken(req);
        return res.status(auth.status).json(auth)
    }

    async findByEmail(req, res) {
        let user = await UserService.findByEmail(req);
        return res.json(user);
    }
}

export default new UserController();
import { Router } from "express";

import UserController from "../controller/UserController.js";
import checkToken from "../../../config/auth/check-token.js";

const router = new Router();

router.post("/user/auth", UserController.getAccessToken);
router.use(checkToken)
router.get("/user/email/:email", UserController.findByEmail);


export default router;
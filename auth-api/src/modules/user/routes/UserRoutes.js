import { Router } from "express";

import UserController from "../controller/UserController.js";

const router = new Router();

router.get("/user/email/:email", UserController.findByEmail);
router.post("/user/auth", UserController.getAccessToken);


export default router;
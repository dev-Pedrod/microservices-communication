import express from "express"
import * as db from "./src/config/db/initial-data.js"
import userRouter from "./src/modules/user/routes/UserRoutes.js";
import checkToken from "./src/config/auth/check-token.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData().then(() => {
    console.info("Initial data has ben created.")
});

app.use(express.json())

app.get('/api/status', (req, res) => {
    return res.json({
        service: 'auth-api',
        status: 'up',
        httpStatus: 200
    })
})

app.use("/api", userRouter);

app.listen(PORT, () => {
    console.info(`Server started successfully at port: ${PORT}`);
})
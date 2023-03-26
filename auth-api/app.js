import express from "express"
import * as db from "./src/config/db/initial-data.js"

const app = express();
const env = process.env;
const PORT = env.PORT || 8080;

db.createInitialData().then(r => {
    console.info("Initial data has ben created.")
});

app.get('/api/status', (req, res) => {
    return res.json({
        service: 'auth-api',
        status: 'up',
        httpStatus: 200
    })
})

app.listen(PORT, () => {
    console.info(`Server started successfully at port: ${PORT}`);
})
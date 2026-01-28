import { Elysia } from "elysia";
import { userController } from "./src/controllers/userController";

const app = new Elysia();

app.use(userController);

app.listen(3000);

console.log("Server started at http://localhost:3000");
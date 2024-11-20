import { mount } from 'svelte'
import './app.css'
import App from './App.svelte'
import {CallHandler} from "./services/callHandler.js";
import {SpringApi} from "./services/api/springApi.js";

const callHandler = new CallHandler(new SpringApi());

export const api = callHandler;

const app = mount(App, {
  target: document.getElementById('app'),
})

export default app

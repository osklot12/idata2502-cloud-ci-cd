import { mount } from 'svelte'
import './app.css'
import App from './App.svelte'
import {CallHandler} from "./callHandler.js";
import {SpringApi} from "./springApi.js";

const callHandler = new CallHandler(new SpringApi());

export const api = callHandler;

const app = mount(App, {
  target: document.getElementById('app'),
})

export default app

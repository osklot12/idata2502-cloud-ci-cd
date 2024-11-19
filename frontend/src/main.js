import { mount } from 'svelte'
import './app.css'
import App from './App.svelte'
import {CallHandler} from "./callHandler.js";
import {createApi} from "./apiFactory.js";

const apiInstance = createApi();
const callHandler = new CallHandler(apiInstance);

export const api = callHandler;

const app = mount(App, {
  target: document.getElementById('app'),
})

export default app

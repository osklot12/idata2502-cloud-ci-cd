// apiFactory.js
import { SpringApi } from './springApi.js';
import { TestApi } from './testApi';

export function createApi() {
    if (process.env.NODE_ENV === 'test') {
        // Use TestApi for testing
        return new TestApi();
    }
    // Use RealApi for production and development
    return new SpringApi();
}

import { describe, it, expect } from 'vitest';
import User from '../../classes/User.js';

describe('User', () => {
  it('should initialize properties correctly', () => {
    const userData = {
      id: 1,
      username: 'admin',
      email: 'admin@example.com'
    };

    const user = new User(userData);

    expect(user.id).toBe(1);
    expect(user.username).toBe('admin');
    expect(user.email).toBe('admin@example.com');
  });

  it('should handle missing properties', () => {
    const user = new User();
    expect(user.id).toBeNull();
    expect(user.username).toBe('');
    expect(user.email).toBe('');
  });

  it('should handle partial initialization', () => {
    const user = new User({ username: 'admin' });
    expect(user.id).toBeNull();
    expect(user.username).toBe('admin');
    expect(user.email).toBe('');
  });

  it('should handle invalid property types', () => {
    const user = new User({ id: 'string', email: 123 });
    expect(user.id).toBeNull();
    expect(user.email).toBe('');
  });

  it('should return display name correctly', () => {
    const userWithUsername = new User({ username: 'admin' });
    expect(userWithUsername.getDisplayName()).toBe('admin');

    const userWithoutUsername = new User();
    expect(userWithoutUsername.getDisplayName()).toBe('Unknown user');
  });

  it('should handle extremely long usernames gracefully', () => {
    const longUsername = 'a'.repeat(500);
    const user = new User({ username: longUsername });
    expect(user.username).toBe(longUsername);
    expect(user.getDisplayName()).toBe(longUsername);
  });
});

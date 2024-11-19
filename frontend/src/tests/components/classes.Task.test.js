import { describe, it, expect } from 'vitest';
import Task from '../../classes/Task.js';
import User from '../../classes/User.js';

describe('Task', () => {
  it('should initialize properties correctly', () => {
    const taskData = {
      id: 1,
      header: 'Test Task',
      description: 'This is a test task',
      status: 'pending',
      deadline: '2024-11-08',
      createdAt: '2023-11-08T12:00:00Z',
      creator: { id: 1, username: 'admin' },
      assignees: [{ id: 2, username: 'Ole' }]
    };

    const task = new Task(taskData);

    expect(task.id).toBe(1);
    expect(task.header).toBe('Test Task');
    expect(task.description).toBe('This is a test task');
    expect(task.status).toBe('pending');
    expect(task.deadline).toEqual(new Date('2024-11-08'));
    expect(task.createdAt).toEqual(new Date('2023-11-08T12:00:00Z'));
    expect(task.creator).toBeInstanceOf(User);
    expect(task.assignees[0]).toBeInstanceOf(User);
  });

  it('should update status correctly', () => {
    const task = new Task({ status: 'pending' });
    task.updateStatus('completed');
    expect(task.status).toBe('completed');
  });

  it('should format deadline correctly', () => {
    const taskWithDeadline = new Task({ deadline: '2024-11-08' });
    expect(taskWithDeadline.getFormattedDeadline()).toBe(new Date('2024-11-08').toLocaleDateString());

    const taskWithoutDeadline = new Task();
    expect(taskWithoutDeadline.getFormattedDeadline()).toBe('No deadline');
  });

  it('should format createdAt correctly', () => {
    const task = new Task({ createdAt: '2023-11-08T12:00:00Z' });
    expect(task.getFormattedCreatedAt()).toBe(new Date('2023-11-08T12:00:00Z').toLocaleString());
  });

  it('should return creator name correctly', () => {
    const task = new Task({ creator: { id: 1, username: 'admin' } });
    expect(task.getCreatorName()).toBe('admin');
  });

  it('should return assignees names correctly', () => {
    const task = new Task({ assignees: [{ id: 2, username: 'Ole' }, { id: 3, username: 'Per' }] });
    expect(task.getAssigneesNames()).toBe('Ole, Per');
  });
});
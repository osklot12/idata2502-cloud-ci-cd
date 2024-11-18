<<<<<<< HEAD
import { render, fireEvent } from '@testing-library/svelte';
import TaskList from '../../components/TaskList.svelte';
import { expect, test } from 'vitest';
import { vi } from 'vitest';
=======
import { render } from '@testing-library/svelte';
import TaskList from '$components/task/TaskList.svelte';
import { test, expect } from 'vitest';
>>>>>>> frontend

test('renders TaskList component', () => {
  const { getByText } = render(TaskList);

  expect(getByText('Task List')).toBeInTheDocument();
});

test('renders tasks in TaskList component', () => {
  const { getByText } = render(TaskList);

  expect(getByText('Task 1')).toBeInTheDocument();
  expect(getByText('Task 2')).toBeInTheDocument();
  expect(getByText('Task 3')).toBeInTheDocument();
});

test('updates task status in TaskList', async () => {
  const { getByText, getByLabelText } = render(TaskList);

  const statusSelect = getByLabelText('Status:') as HTMLSelectElement;
  await fireEvent.change(statusSelect, { target: { value: 'completed' } });

  expect(statusSelect.value).toBe('completed');
});


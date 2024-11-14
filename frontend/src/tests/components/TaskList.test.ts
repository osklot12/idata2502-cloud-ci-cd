import { render } from '@testing-library/svelte';
import TaskList from '$components/task/TaskList.svelte';
import { test, expect } from 'vitest';

test('renders TaskList component', () => {
  const { getByText } = render(TaskList);

  expect(getByText('Task List')).toBeInTheDocument();
});
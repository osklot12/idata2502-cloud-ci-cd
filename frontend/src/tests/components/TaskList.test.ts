import { render } from '@testing-library/svelte';
import TaskList from '../../components/TaskList.svelte';
import { test, expect } from 'vitest';

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
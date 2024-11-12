import { render, fireEvent } from '@testing-library/svelte';
import TaskCard from '../../components/TaskCard.svelte';
import TaskList from '../../components/TaskList.svelte';
import { expect, test } from 'vitest';
import { vi } from 'vitest';


test('renders TaskCard component with initial props', () => {
    const { getByText, getByLabelText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onStatusChange: vi.fn()
        }
    });

    expect(getByText('Task 1')).toBeInTheDocument();
    expect(getByText('Description of Task 1')).toBeInTheDocument();
    expect(getByText('Due: 2024-11-08')).toBeInTheDocument();
    expect(getByText('Assigned to: John Doe')).toBeInTheDocument();
    expect(getByLabelText('Status:').value).toBe('pending');
});

test('allows changing task status', async () => {
    const { getByLabelText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onStatusChange: vi.fn()
        }
    });

    const statusSelect = getByLabelText('Status:') as HTMLSelectElement;
    await fireEvent.change(statusSelect, { target: { value: 'completed' } });

    expect(statusSelect.value).toBe('completed');
});


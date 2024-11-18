import { render, fireEvent } from '@testing-library/svelte';
import TaskCard from '$components/task/TaskCard.svelte';
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
    const onStatusChange = vi.fn();
    const { getByLabelText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onStatusChange: onStatusChange
        }
    });

    const statusSelect = getByLabelText('Status:') as HTMLSelectElement;
    await fireEvent.change(statusSelect, { target: { value: 'completed' } });

    expect(statusSelect.value).toBe('completed');
    expect(onStatusChange).toHaveBeenCalledWith('completed');
});

test('removes a task in TaskCard', async () => {
    const onRemove = vi.fn();
    const { getByText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onRemove
        }
    });

    const removeButton = getByText('✕');
    await fireEvent.click(removeButton);

    expect(onRemove).toHaveBeenCalled();
});



import { render, fireEvent } from '@testing-library/svelte';
import TaskCard from '$components/task/TaskCard.svelte';
import { expect, test } from 'vitest';
import { vi } from 'vitest';

test('allows editing task details', async () => {
    const { getByText, getByLabelText, getByPlaceholderText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onEdit: vi.fn()
        },
        hydrate: false
    })

 
    await fireEvent.click(getByText('Edit'))
    await fireEvent.input(getByPlaceholderText('Task title'), { target: { value: 'Updated Task 1' } })
    await fireEvent.input(getByPlaceholderText('Task description'), { target: { value: 'Updated Description of Task 1' } })
    await fireEvent.input(getByLabelText('Due Date'), { target: { value: '2024-12-12' } })
    await fireEvent.change(getByLabelText('Assign to'), { target: { value: 'Jane Doe' } })
    await fireEvent.click(getByText('Save'))

 
    expect(getByText('Updated Task 1')).toBeInTheDocument()
    expect(getByText('Updated Description of Task 1')).toBeInTheDocument()
    expect(getByText('Due: 2024-12-12')).toBeInTheDocument()
    expect(getByText('Assigned to: Jane Doe')).toBeInTheDocument()
})

test('allows deleting a task', async () => {
    const { getByText } = render(TaskCard, {
        props: {
            title: 'Task 1',
            description: 'Description of Task 1',
            dueDate: '2024-11-08',
            assignedTo: 'John Doe',
            status: 'pending',
            onDelete: vi.fn()
        },
        hydrate: false
    })


    await fireEvent.click(getByText('Delete'))

 
    expect(getByText('Task 1')).not.toBeInTheDocument()
})
describe('task page', () => {
  beforeEach(() => {
    cy.visit('http://35.228.18.196/#/tasks')

    cy.get('input[placeholder="Username"]').type('tester');

    cy.get('input[placeholder="Password"]').type('tester123');

    cy.get('button.std-form-element.std-form-btn[type="submit"]').click();
  });
  
  it('logs into taskmanager and creates a task', () => {
    
    cy.get('input[placeholder="header"]').type('First task').should('have.value', 'First task');
  
    cy.get('textarea[placeholder="description"]').type('This is the first task').should('have.value', 'This is the first task');

    cy.get('input[type="date"]').type('2024-12-24').should('have.value', '2024-12-24');

    cy.get('input[placeholder="username"]').type('tester').should('have.value', 'tester');

    cy.get('button.std-form-btn.task-creator-option').click();
  });

  it('should expand the task when the expand button is clicked', () => {

    cy.get('span.material-symbols-outlined').contains('expand_content').click();

    cy.get('span.material-symbols-outlined').should('contain.text', 'collapse_content');
  });

});
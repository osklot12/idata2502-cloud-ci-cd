describe('login page', () => {

  // Checks if both input fields are working correctly
  it('checks if input fields work visually', () => {
    
    cy.visit('http://35.228.18.196/#/auth/login');
  
    cy.get('input[placeholder="Username"]').type('TestName').should('have.value', 'TestName');

    cy.get('input[placeholder="Password"]').type('TestPassword').should('have.value', 'TestPassword');
  });

  it('should navigate to the registration page when clicking the register link', () => {
    cy.visit('http://35.228.18.196/#/auth/login');

    // Click the link
    cy.get('a[href="#/auth/register"]')
      .click();

    cy.url().should('include', '#/auth/register'); // Checks that the URL contains '#/auth/register'

    cy.get('input[placeholder="Username"]').type('tester').should('have.value', 'tester');

      cy.get('input[placeholder="Email"]').type('tester@gmail.com').should('have.value', 'tester@gmail.com');
  
      cy.get('input[placeholder="Password"]').type('tester123').should('have.value', 'tester123');

      cy.get('button.std-form-element.std-form-btn[type="submit"]').click();
  });

  it('should navigate to the correct page after login', () => {

    cy.visit('http://35.228.18.196/#/auth/login');
    
    cy.get('input[placeholder="Username"]').type('tester');

    cy.get('input[placeholder="Password"]').type('tester123');

    cy.get('button.std-form-element.std-form-btn[type="submit"]').click();

    cy.url().should('include', '#/tasks');
  });
});
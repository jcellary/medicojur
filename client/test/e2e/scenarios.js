'use strict';

/* https://github.com/angular/protractor/blob/master/docs/getting-started.md */

describe('rune age', function() {

    browser.get('index.html');

    it('should automatically redirect to /welcome when location hash/fragment is empty', function() {
        expect(browser.getLocationAbsUrl()).toMatch("/welcome");
    });


    describe('welcome', function() {

        beforeEach(function() {
            browser.get('index.html#/welcome');
        });


        it('should render welcome when user navigates to /welcome', function() {
            expect(element.all(by.css('[ng-view] h1')).first().getText()).
                toMatch(/Welcome to Rune Age!/);
        });

    });

/*
    describe('game', function() {

        beforeEach(function() {
            browser.get('index.html#/game');
        });


        it('should render game when user navigates to /game', function() {
            expect(element.all(by.css('[ng-view] p')).first().getText()).
                toMatch(/partial for view 2/);
        });

    });*/
});

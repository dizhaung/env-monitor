(function () {
    'use strict';

    angular
        .module('queryLib')
        .config(routersConfig);

    routersConfig.$inject = ['$stateProvider'];

    function routersConfig($stateProvider) {
        $stateProvider
            .state('query_dashboard', {
                url: "/query_dashboard",
                templateUrl: "/app/modules/queryLib/templates/dashboard.html"
            })
    }
})();
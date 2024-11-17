-For amount transfer functionality I have added a post webservice endpoint as 'v1/accounts/transfer'.This webservice is taking
input as TransferRequest which has field as accountFrom, accountTo and amount.
-All business logic and validations is in AccountsService class.


-If given more time I would have created custom exceptions like InsufficientBalanceException, AccountNotFoundException and IllegalAmountTransferException.
-I would have created a response object to return more meaningfull response.
-I Would have created Exception handler class where we would have returned meaningfull response after throwing validation exception.
-I Would have covered more test cases.

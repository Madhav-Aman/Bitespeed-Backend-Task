# Backend Task API

## Overview
This project implements a RESTful API for identifying contact information based on email and/or phone number. The API is hosted at 
[https://bitespeed-backend-task-production-2e8e.up.railway.app/api/v1/identify](https://bitespeed-backend-task-production-2e8e.up.railway.app/api/v1/identify).

## Endpoint
- **POST /api/v1/identify**: This endpoint accepts a JSON request containing either an email or a phone number, or both, and returns contact information associated with the provided details.

### Request Format
```json
{
	"email"?: string,
	"phoneNumber"?: number
}
```

The `email` field should contain a valid email address.  
The `phoneNumber` field should contain a valid phone number. If no phone number is provided, it should be an empty string.

### Response Format
```json
{
	"contact":{
		"primaryContatctId": number,
		"emails": string[], // first element being email of primary contact 
		"phoneNumbers": string[], // first element being phoneNumber of primary contact
		"secondaryContactIds": number[] // Array of all Contact IDs that are "secondary" to the primary contact
	}
}
```

## Usage
Send a POST request to the endpoint with the appropriate JSON request format to fetch contact information.

## Dependencies
- Spring Boot
- Spring Data JPA
- SLF4J for logging
- MysqlConnector


## Production Branch
	The production branch name is 'deploy'


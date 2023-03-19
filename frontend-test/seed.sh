USERNAME="bob2@mail.com"
# Create user
curl --location --request POST '0.0.0.0:8080/user/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "'$USERNAME'",
    "raw_password": "1234"
}'

# Login user and get token
LOGIN_RSP=$(curl --location --request POST '0.0.0.0:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "'$USERNAME'",
    "password": "1234"
}')
TOKEN=$(jq -r '.access_token' <<<"$LOGIN_RSP")
echo "Received token: $TOKEN"

# Create note
curl --location --request POST '0.0.0.0:8080/note' \
--header "Authorization: Bearer $TOKEN" \
--header 'Content-Type: application/json' \
--data-raw '{
    "title": "my first note",
    "content": "my note content :D lalala"
}'

# Search a note
SEARCH_RSP=curl --location --request GET '0.0.0.0:8080/note/findsec?title=my%20first%20note' \
--header "Authorization: Bearer $TOKEN" \--header 'Content-Type: application/json'

echo "Search rsp: $SEARCH_RSP"
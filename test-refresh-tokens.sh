#!/bin/bash

# MechTrack Refresh Token Testing Script
# This script tests the new refresh token functionality

BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api/auth"

echo "🔧 MechTrack Refresh Token Testing"
echo "=================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test credentials (update these to match your environment variables)
USERNAME="your_username_here"
PASSWORD="your_password_here"

echo "📋 Test Configuration:"
echo "  Base URL: $BASE_URL"
echo "  Username: $USERNAME"
echo "  Password: [HIDDEN]"
echo ""

# Function to make HTTP requests with error handling
make_request() {
    local method=$1
    local url=$2
    local data=$3
    local headers=$4
    
    if [ -n "$data" ]; then
        if [ -n "$headers" ]; then
            curl -s -X "$method" "$url" -H "Content-Type: application/json" -H "$headers" -d "$data"
        else
            curl -s -X "$method" "$url" -H "Content-Type: application/json" -d "$data"
        fi
    else
        if [ -n "$headers" ]; then
            curl -s -X "$method" "$url" -H "$headers"
        else
            curl -s -X "$method" "$url"
        fi
    fi
}

# Function to extract JSON value
extract_json_value() {
    local json=$1
    local key=$2
    echo "$json" | grep -o "\"$key\":\"[^\"]*\"" | cut -d'"' -f4
}

echo "🔐 Step 1: Testing Login (should return both access and refresh tokens)"
echo "=================================================================="

LOGIN_DATA="{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}"
LOGIN_RESPONSE=$(make_request "POST" "$API_BASE/login" "$LOGIN_DATA")

echo "Response:"
echo "$LOGIN_RESPONSE" | jq . 2>/dev/null || echo "$LOGIN_RESPONSE"
echo ""

# Extract tokens
ACCESS_TOKEN=$(extract_json_value "$LOGIN_RESPONSE" "accessToken")
REFRESH_TOKEN=$(extract_json_value "$LOGIN_RESPONSE" "refreshToken")

if [ -n "$ACCESS_TOKEN" ] && [ -n "$REFRESH_TOKEN" ]; then
    echo -e "${GREEN}✅ Login successful! Got both tokens.${NC}"
    echo "Access Token: ${ACCESS_TOKEN:0:50}..."
    echo "Refresh Token: ${REFRESH_TOKEN:0:50}..."
else
    echo -e "${RED}❌ Login failed or tokens missing!${NC}"
    exit 1
fi

echo ""
echo "🔄 Step 2: Testing Token Refresh"
echo "================================"

REFRESH_DATA="{\"refreshToken\":\"$REFRESH_TOKEN\"}"
REFRESH_RESPONSE=$(make_request "POST" "$API_BASE/refresh" "$REFRESH_DATA")

echo "Response:"
echo "$REFRESH_RESPONSE" | jq . 2>/dev/null || echo "$REFRESH_RESPONSE"
echo ""

# Extract new tokens
NEW_ACCESS_TOKEN=$(extract_json_value "$REFRESH_RESPONSE" "accessToken")
NEW_REFRESH_TOKEN=$(extract_json_value "$REFRESH_RESPONSE" "refreshToken")

if [ -n "$NEW_ACCESS_TOKEN" ] && [ -n "$NEW_REFRESH_TOKEN" ]; then
    echo -e "${GREEN}✅ Token refresh successful! Got new tokens.${NC}"
    echo "New Access Token: ${NEW_ACCESS_TOKEN:0:50}..."
    echo "New Refresh Token: ${NEW_REFRESH_TOKEN:0:50}..."
    
    # Verify tokens are different
    if [ "$ACCESS_TOKEN" != "$NEW_ACCESS_TOKEN" ] && [ "$REFRESH_TOKEN" != "$NEW_REFRESH_TOKEN" ]; then
        echo -e "${GREEN}✅ Token rotation working - new tokens are different!${NC}"
    else
        echo -e "${YELLOW}⚠️  Warning: New tokens are the same as old ones${NC}"
    fi
else
    echo -e "${RED}❌ Token refresh failed!${NC}"
fi

echo ""
echo "🔒 Step 3: Testing Access Token Usage"
echo "===================================="

# Test using the new access token to validate
VALIDATE_RESPONSE=$(make_request "POST" "$API_BASE/validate" "" "Authorization: Bearer $NEW_ACCESS_TOKEN")

echo "Validation Response:"
echo "$VALIDATE_RESPONSE" | jq . 2>/dev/null || echo "$VALIDATE_RESPONSE"

if echo "$VALIDATE_RESPONSE" | grep -q '"valid":true'; then
    echo -e "${GREEN}✅ New access token is valid!${NC}"
else
    echo -e "${RED}❌ New access token validation failed!${NC}"
fi

echo ""
echo "🚪 Step 4: Testing Logout (Token Revocation)"
echo "============================================="

LOGOUT_DATA="{\"refreshToken\":\"$NEW_REFRESH_TOKEN\"}"
LOGOUT_RESPONSE=$(make_request "POST" "$API_BASE/logout" "$LOGOUT_DATA")

echo "Logout Response:"
echo "$LOGOUT_RESPONSE" | jq . 2>/dev/null || echo "$LOGOUT_RESPONSE"

if echo "$LOGOUT_RESPONSE" | grep -q '"message":"Logout successful"'; then
    echo -e "${GREEN}✅ Logout successful!${NC}"
else
    echo -e "${RED}❌ Logout failed!${NC}"
fi

echo ""
echo "🔄 Step 5: Testing Refresh with Revoked Token (should fail)"
echo "=========================================================="

REVOKED_REFRESH_DATA="{\"refreshToken\":\"$NEW_REFRESH_TOKEN\"}"
REVOKED_RESPONSE=$(make_request "POST" "$API_BASE/refresh" "$REVOKED_REFRESH_DATA")

echo "Response with revoked token:"
echo "$REVOKED_RESPONSE" | jq . 2>/dev/null || echo "$REVOKED_RESPONSE"

if echo "$REVOKED_RESPONSE" | grep -q '"status":401'; then
    echo -e "${GREEN}✅ Revoked token correctly rejected!${NC}"
else
    echo -e "${RED}❌ Revoked token was accepted (security issue!)${NC}"
fi

echo ""
echo "🧪 Step 6: Testing Invalid Refresh Token (should fail)"
echo "====================================================="

INVALID_DATA="{\"refreshToken\":\"invalid.token.here\"}"
INVALID_RESPONSE=$(make_request "POST" "$API_BASE/refresh" "$INVALID_DATA")

echo "Response with invalid token:"
echo "$INVALID_RESPONSE" | jq . 2>/dev/null || echo "$INVALID_RESPONSE"

if echo "$INVALID_RESPONSE" | grep -q '"status":401'; then
    echo -e "${GREEN}✅ Invalid token correctly rejected!${NC}"
else
    echo -e "${RED}❌ Invalid token was accepted (security issue!)${NC}"
fi

echo ""
echo "📊 Test Summary"
echo "==============="
echo -e "${GREEN}✅ Tests completed!${NC}"
echo ""
echo "Expected Results:"
echo "1. Login should return both accessToken and refreshToken"
echo "2. Refresh should return new token pair"
echo "3. New access token should be valid"
echo "4. Logout should succeed"
echo "5. Revoked refresh token should be rejected"
echo "6. Invalid refresh token should be rejected"
echo ""
echo "🔧 Next Steps:"
echo "- Update USERNAME and PASSWORD variables in this script"
echo "- Start your MechTrack application"
echo "- Run: chmod +x test-refresh-tokens.sh && ./test-refresh-tokens.sh"

#!/bin/bash

# ============================================
# Baozí Store API - Run Script
# ============================================
# This script automates the setup and execution
# of the Baozí Store Spring Boot application
# ============================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVA_VERSION="17"
MAVEN_VERSION="3.8+"
MYSQL_USER="root"
MYSQL_PASSWORD="root"
MYSQL_DATABASE="baozistore"
APP_PORT="8080"

# ============================================
# Helper Functions
# ============================================

print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

check_command() {
    if ! command -v "$1" &> /dev/null; then
        print_error "$1 is not installed. Please install it first."
        return 1
    else
        print_success "$1 is installed"
        return 0
    fi
}

wait_for_mysql() {
    print_info "Waiting for MySQL to be ready..."
    for i in {1..30}; do
        if mysqladmin ping -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent 2>/dev/null; then
            print_success "MySQL is ready"
            return 0
        fi
        sleep 1
    done
    print_error "MySQL is not responding"
    return 1
}

# ============================================
# Main Functions
# ============================================

check_prerequisites() {
    print_header "Checking Prerequisites"
    
    # Check Java
    if check_command "java"; then
        JAVA_VERSION_INSTALLED=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
        if [ "$JAVA_VERSION_INSTALLED" -ge "$JAVA_VERSION" ]; then
            print_success "Java version: $(java -version 2>&1 | head -n1)"
        else
            print_warning "Java version $JAVA_VERSION or higher is recommended (found: $JAVA_VERSION_INSTALLED)"
        fi
    else
        print_error "Java is not installed. Please install Java $JAVA_VERSION or higher."
        exit 1
    fi

    # Check Maven
    if check_command "mvn"; then
        print_success "Maven version: $(mvn -version | head -n1)"
    else
        print_error "Maven is not installed. Please install Maven $MAVEN_VERSION or higher."
        exit 1
    fi

    # Check MySQL
    if check_command "mysql"; then
        print_success "MySQL version: $(mysql --version)"
    else
        print_warning "MySQL client is not installed. You may need to install MySQL."
        print_info "For H2 database (dev mode), this is not required."
    fi

    # Check git
    if check_command "git"; then
        print_success "Git version: $(git --version)"
    fi

    # Check curl
    if check_command "curl"; then
        print_success "Curl is installed"
    fi

    echo ""
}

setup_database() {
    print_header "Setting up MySQL Database"
    
    if ! command -v mysql &> /dev/null; then
        print_warning "MySQL client not found. Skipping database setup."
        print_info "Using H2 database in development mode."
        return 0
    fi

    # Check if MySQL is running
    if ! mysqladmin ping -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent 2>/dev/null; then
        print_warning "MySQL is not running or credentials are incorrect."
        print_info "Please ensure MySQL is running and check credentials in application.properties"
        print_info "Or use H2 database by enabling dev profile"
        return 0
    fi

    # Create database if not exists
    print_info "Creating database '$MYSQL_DATABASE' if it doesn't exist..."
    mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        print_success "Database '$MYSQL_DATABASE' is ready"
        
        # Optional: Show existing tables
        TABLES=$(mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D"$MYSQL_DATABASE" -e "SHOW TABLES;" 2>/dev/null | wc -l)
        if [ "$TABLES" -gt 1 ]; then
            print_info "Found existing tables in the database"
        fi
    else
        print_warning "Could not create database. Please check MySQL credentials."
        print_info "You can update credentials in application.properties"
    fi
    
    echo ""
}

build_project() {
    print_header "Building the Project"
    
    print_info "Running: mvn clean compile"
    if mvn clean compile -q; then
        print_success "Project compiled successfully"
    else
        print_error "Failed to compile the project"
        exit 1
    fi
    
    echo ""
}

run_tests() {
    print_header "Running Tests"
    
    print_info "Running: mvn test"
    if mvn test -q; then
        print_success "All tests passed"
    else
        print_warning "Some tests failed. You can still run the application."
        print_info "To skip tests, use: ./run.sh --skip-tests"
    fi
    
    echo ""
}

run_application() {
    print_header "Starting Baozí Store API"
    
    print_info "Application will run on: http://localhost:$APP_PORT"
    print_info "Press Ctrl+C to stop the application"
    echo ""
    
    # Check if we should skip tests
    if [ "$SKIP_TESTS" = true ]; then
        print_info "Skipping tests (--skip-tests flag detected)"
        mvn spring-boot:run -DskipTests
    else
        mvn spring-boot:run
    fi
}

run_with_dev_profile() {
    print_header "Starting Baozí Store API (Development Mode)"
    
    print_info "Using H2 in-memory database"
    print_info "H2 Console: http://localhost:$APP_PORT/h2-console"
    print_info "Application will run on: http://localhost:$APP_PORT"
    print_info "Press Ctrl+C to stop the application"
    echo ""
    
    if [ "$SKIP_TESTS" = true ]; then
        mvn spring-boot:run -Dspring-boot.run.profiles=dev -DskipTests
    else
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
    fi
}

check_application_status() {
    print_header "Checking Application Status"
    
    print_info "Waiting for application to start..."
    sleep 3
    
    # Check if application is running
    if curl -s http://localhost:$APP_PORT/api/clientes > /dev/null 2>&1; then
        print_success "Application is running successfully!"
        echo ""
        print_info "API Endpoints:"
        echo "  - Clientes: http://localhost:$APP_PORT/api/clientes"
        echo "  - Produtos: http://localhost:$APP_PORT/api/produtos"
        echo "  - Pedidos:  http://localhost:$APP_PORT/api/pedidos"
        echo ""
        print_info "H2 Console (dev mode): http://localhost:$APP_PORT/h2-console"
    else
        print_warning "Application may not be ready yet or may have issues"
        print_info "Check the application logs above for details"
    fi
}

show_help() {
    echo "Baozí Store API - Run Script"
    echo ""
    echo "Usage: ./run.sh [options]"
    echo ""
    echo "Options:"
    echo "  --help, -h          Show this help message"
    echo "  --dev, -d           Run in development mode (H2 database)"
    echo "  --prod, -p          Run in production mode (MySQL database)"
    echo "  --skip-tests, -s    Skip running tests"
    echo "  --no-build, -n      Skip build (use existing compiled classes)"
    echo "  --install, -i       Install dependencies and setup only"
    echo "  --test-only, -t     Only run tests"
    echo "  --clean, -c         Clean the project before building"
    echo ""
    echo "Examples:"
    echo "  ./run.sh                     # Build, test, and run with MySQL"
    echo "  ./run.sh --dev               # Run with H2 database"
    echo "  ./run.sh --skip-tests        # Skip tests and run"
    echo "  ./run.sh --test-only         # Only run tests"
    echo "  ./run.sh --clean --dev       # Clean and run in dev mode"
    echo ""
}

# ============================================
# Script Execution
# ============================================

# Parse command line arguments
DEV_MODE=false
PROD_MODE=false
SKIP_TESTS=false
NO_BUILD=false
INSTALL_ONLY=false
TEST_ONLY=false
CLEAN=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --help|-h)
            show_help
            exit 0
            ;;
        --dev|-d)
            DEV_MODE=true
            shift
            ;;
        --prod|-p)
            PROD_MODE=true
            shift
            ;;
        --skip-tests|-s)
            SKIP_TESTS=true
            shift
            ;;
        --no-build|-n)
            NO_BUILD=true
            shift
            ;;
        --install|-i)
            INSTALL_ONLY=true
            shift
            ;;
        --test-only|-t)
            TEST_ONLY=true
            shift
            ;;
        --clean|-c)
            CLEAN=true
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# ============================================
# Main Execution Flow
# ============================================

print_header "Baozí Store API - Setup & Run Script"

# Check if running with proper permissions
if [ ! -x "$0" ]; then
    print_info "Making script executable..."
    chmod +x "$0"
fi

# Check prerequisites
check_prerequisites

# Clean if requested
if [ "$CLEAN" = true ]; then
    print_header "Cleaning Project"
    mvn clean -q
    print_success "Project cleaned"
fi

# Install only mode
if [ "$INSTALL_ONLY" = true ]; then
    setup_database
    build_project
    print_success "Setup completed successfully!"
    exit 0
fi

# Test only mode
if [ "$TEST_ONLY" = true ]; then
    if [ "$NO_BUILD" = false ]; then
        build_project
    fi
    run_tests
    exit 0
fi

# Build the project
if [ "$NO_BUILD" = false ]; then
    build_project
else
    print_info "Skipping build (--no-build flag detected)"
fi

# Run tests (unless skipped)
if [ "$SKIP_TESTS" = false ] && [ "$TEST_ONLY" = false ]; then
    run_tests
fi

# Setup database
if [ "$DEV_MODE" = false ]; then
    setup_database
fi

# Run the application
if [ "$DEV_MODE" = true ] || [ "$PROD_MODE" = false ] && [ -z "$1" ]; then
    # Default to dev mode if no mode specified
    print_info "No mode specified. Running in DEVELOPMENT mode (H2 database)."
    run_with_dev_profile
else
    run_application
fi

# This point is only reached if the application is stopped
check_application_status

print_header "Baozí Store API Stopped"
print_success "Application terminated successfully!"

exit 0
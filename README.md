# UpStox Assignment

Hey! This is my take on the UpStox portfolio assignment. I built a clean Android app that shows stock holdings with a nice portfolio summary. The app fetches real data from an API and displays it in a user-friendly way.

## What it does

- Shows your stock holdings in a scrollable list with all the important details
- Has an expandable portfolio summary card at the bottom that shows:
  - Current value of all holdings
  - Total amount you've invested
  - Today's profit/loss
  - Overall profit/loss with percentage
- Pull down to refresh the data
- Smooth animations when expanding/collapsing the summary
- Proper loading states and error handling

## How I built it

I went with **MVVM architecture** because it keeps things clean and testable. Here's the breakdown:

- **Model**: Just data classes and business logic
- **View**: The UI stuff (MainActivity, layouts)
- **ViewModel**: Handles all the business logic and state management
- **Repository**: Takes care of data fetching and caching

## Tech stack & dependencies

I kept the dependencies minimal but used the right tools for the job:

**Core Android:**
- Kotlin 2.0.21 (latest stable)
- View Binding for type-safe UI access
- Material Design Components for consistent UI

**Architecture & State Management:**
- ViewModel + LiveData for reactive UI updates
- Lifecycle-aware components
- Repository pattern for clean data access

**Networking:**
- Retrofit 2.11.0 for API calls
- OkHttp 4.12.0 with logging interceptor
- Gson for JSON parsing

**Async Operations:**
- Kotlin Coroutines for background work
- Coroutines test library for unit testing

**Testing:**
- JUnit 4.13.2 for basic testing
- MockK 1.13.8 for mocking (works great with Kotlin)
- Architecture Components Testing for LiveData testing
- Coroutines test dispatcher for testing async code

**UI Components:**
- RecyclerView for the holdings list
- SwipeRefreshLayout for pull-to-refresh
- ConstraintLayout for responsive design

## Project structure

Here's how I organized the code:

```
app/src/main/java/com/rahulyadav/upstoxassignment/
├── data/
│   ├── api/                 # API service interfaces
│   ├── model/               # Data classes (Stocks, PortfolioSummary, etc.)
│   └── repository/          # Repository implementation
├── ui/
│   ├── adapter/             # RecyclerView adapter for holdings list
│   └── viewmodel/           # ViewModels and UI state management
├── utils/                   # Helper classes for formatting
├── di/                      # Simple dependency injection
└── MainActivity.kt          # Main activity
```

## Key files explained

**Data Layer:**
- `Stocks.kt` - The main data model for individual stock holdings with calculation methods
- `PortfolioSummary.kt` - Handles portfolio-level calculations (total P&L, current value, etc.)
- `PortfolioRepository.kt` - Fetches data from API and handles errors
- `PortfolioApiService.kt` - Retrofit interface for the API calls

**UI Layer:**
- `MainActivity.kt` - Main activity that handles UI updates and user interactions
- `HoldingsAdapter.kt` - RecyclerView adapter for displaying the holdings list
- `PortfolioViewModel.kt` - Contains all the business logic and state management
- `PortfolioUiState.kt` - Sealed class for managing different UI states (Loading, Success, Error)

**Utils:**
- `DisplayFormatter.kt` - Centralized formatting for currency, percentages, and colors

## The math behind it

I implemented all the required calculations:

1. **Current Value**: Sum of (LTP × Quantity) for all holdings
2. **Total Investment**: Sum of (Average Price × Quantity) for all holdings  
3. **Total P&L**: Current Value - Total Investment
4. **Today's P&L**: Sum of ((Close - LTP) × Quantity) for all holdings

The calculations are done in the `PortfolioSummary` class and individual stock calculations are in the `Stocks` data class.

## API

The app fetches data from: `https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/`

It's a simple GET request that returns a JSON with the holdings data.

## Testing

I wrote unit tests for the important parts:
- Repository tests with mocked API responses
- ViewModel tests to make sure the business logic works
- Model tests for the calculation methods

To run the tests:
```bash
./gradlew test
```

## What I focused on

✅ **Clean Architecture** - MVVM with proper separation of concerns  
✅ **Modern Kotlin** - Using the latest features and best practices  
✅ **Material Design** - Following Google's design guidelines  
✅ **Performance** - Efficient RecyclerView, proper memory management  
✅ **Testability** - Everything is testable with proper mocking  
✅ **Error Handling** - Graceful handling of network errors and edge cases  
✅ **SOLID Principles** - Single responsibility, dependency inversion, etc.  
✅ **Minimal Dependencies** - Only what's necessary, no bloat  

## Setup

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Kotlin**: 2.0.21
- **Gradle**: 8.11.1

Just clone the repo, open in Android Studio, sync, and run. Should work out of the box.

## What the app looks like

- Clean header with "Portfolio" title
- Scrollable list showing all your holdings with symbol, quantity, LTP, and P&L
- Expandable summary card at the bottom with total calculations
- Pull down to refresh
- Loading spinner while fetching data
- Error message if something goes wrong




# -------- Stage 1: Build Angular App --------
FROM node:22-alpine AS build

WORKDIR /app

# Copy dependency files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy project files
COPY . .

# Build Angular for production
RUN npm run build --configuration=production


# -------- Stage 2: Serve with Nginx --------
FROM nginx:alpine

# Copy built Angular app to Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]

FROM node:14.8.0-alpine3.11 As builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

ENV PATH /usr/src/app/node_modules/.bin:$PATH

COPY package.json package-lock.json ./
RUN npm install
RUN npm install -g @angular/cli@latest
COPY . .
RUN ng build --prod
FROM nginx:1.19.2-alpine
COPY --from=builder /usr/src/app/dist/ninety-nine-frontend /usr/share/nginx/html
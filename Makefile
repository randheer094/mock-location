.PHONY: e2e e2e-doctor e2e-install

e2e-install:
	cd e2e && npm install

e2e-doctor:
	cd e2e && npm run doctor

e2e:
	./gradlew :app:assembleDebug
	cd e2e && npm run test
